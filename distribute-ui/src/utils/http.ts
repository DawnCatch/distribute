import { Body, fetch, HttpVerb, ResponseType } from "@tauri-apps/api/http";

function getToken(): string | null {
    return window.localStorage.getItem("token");
}

function setToken(value: string): void {
    window.localStorage.setItem("token", value);
}

type TauriFetch = (opts?: HttpOption) => Promise<unknown>;

interface HttpOption {
    url: string;
    method?: HttpVerb | null;
    query?: Record<string, any>;
    data: Record<string, any>;
    headers?: Record<string, any>;
    callback: (res: HttpResponse) => void;
}

interface HttpResponse {
    status: boolean;
    message: string | null;
    data: Record<string, any>;
}

const baseURL = "http://127.0.0.1";

const http = (opts = {} as HttpOption) => {
    return new Promise((resolve, reject) => {
        const { url, method, query, data, headers, callback } = opts;
        console.log({
            "Content-Type": "application/json;charset=utf-8",
            Authorization: getToken(),
            ...headers,
        });
        fetch(baseURL + url, {
            method: method || "GET",
            headers: {
                "Content-Type": "application/json;charset=utf-8",
                // Authorization: getToken(),
                ...headers,
            },
            responseType: ResponseType.JSON,
            timeout: 10000,
            query: query,
            body: Body.json(data),
        })
            .then((res) => {
                const token = res.headers.authorization;
                if (token) setToken(token);
                console.log(res.data);
                callback(res.data as HttpResponse);
                resolve(res.data as HttpResponse);
            })
            // .catch(() => {
            //     const res = {
            //         status: false,
            //         message: "error",
            //         data: {},
            //     } as HttpResponse;
            //     reject(res);
            // });
    });
};

export { http };
export type { TauriFetch, HttpResponse };
