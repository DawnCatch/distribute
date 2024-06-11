import { Body, fetch, HttpVerb, ResponseType } from "@tauri-apps/api/http";
import { notification } from "./notification";
import { getToken, setToken } from "./secure";
import { ip, platform, port, proxy_rewrite, security } from "./env";
import axios from "axios";

type TauriFetch = (opts?: HttpOption) => Promise<HttpResponse>;

interface HttpOption {
    url: string;
    method?: HttpVerb | null;
    query?: Record<string, any>;
    data?: Record<string, any>;
    headers?: Record<string, any>;
}

interface HttpResponse {
    status: boolean;
    message: string | null;
    data: Record<string, any>;
}

const baseURL = `http${security ? "s" : ""}://${ip}`;

const http = (opts = {} as HttpOption): Promise<HttpResponse> => {
    return new Promise((resolve) => {
        const { url, method, query, data, headers } = opts;
        if (platform) {
            fetch(baseURL + (port !== ''? `:${port}`: '') + url, {
                method: method || "GET",
                headers: {
                    "Content-Type": "application/json;charset=utf-8",
                    Authorization: getToken(),
                    ...headers,
                },
                responseType: ResponseType.JSON,
                timeout: 10000,
                query: query,
                body: Body.json({
                    ...data,
                }),
            }).then((res) => {
                const token = res.headers.authorization;
                if (token) setToken(token);
                let data = res.data as HttpResponse;
                if (!data.status) {
                    notification(data.message as string);
                }
                resolve(data);
            });
        } else {
            axios({
                url: proxy_rewrite + url,
                method: method || "GET",
                data: {
                    ...data,
                },
                headers: {
                    "Content-Type": "application/json;charset=utf-8",
                    Authorization: getToken(),
                    ...headers,
                },
            }).then((res) => {
                const token = res.headers.authorization;
                if (token) setToken(token);
                let data = res.data as HttpResponse;
                if (!data.status) {
                    notification(data.message as string);
                }
                resolve(data);
            });
        }
    });
};

export { http };
export type { TauriFetch, HttpResponse };
