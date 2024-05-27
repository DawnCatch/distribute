import { http } from "./http";

const createPeer = (
    userId: number,
    stream: MediaStream,
    localVideo: HTMLVideoElement,
    remoteVideo: HTMLVideoElement
): Promise<RTCPeerConnection> => {
    return new Promise((resolve) => {
        const connect = new RTCPeerConnection();
        localVideo.srcObject = stream;
        connect.addEventListener("icecandidate", (e) => {
            if (e.candidate) sendICE(userId, e.candidate);
        });
        connect.addEventListener("iceconnectionstatechange", (e) => {
            console.log("peer: iceconnectionstatechange", e);
        });
        connect.addEventListener("track", (e) => {
            if (e.streams.length > 0) {
                remoteVideo.srcObject = e.streams[0];
            }
        });
        stream.getTracks().forEach((track) => connect.addTrack(track, stream));
        connect.createOffer().then((peer) => {
            http({
                url: "/message/send",
                data: {
                    to: userId,
                    content: [
                        {
                            type: "RTC:REQ",
                            value: JSON.stringify(peer),
                        },
                    ],
                },
            });
            connect.setLocalDescription(peer);
            resolve(connect);
        });
    });
};

const createAnswer = (
    userId: number,
    stream: MediaStream,
    session: RTCSessionDescriptionInit,
    localVideo: HTMLVideoElement,
    remoteVideo: HTMLVideoElement
): Promise<RTCPeerConnection> => {
    return new Promise((resolve) => {
        localVideo.srcObject = stream;
        const connect = new RTCPeerConnection();
        stream.getTracks().forEach((track) => connect.addTrack(track, stream));
        connect.onicecandidate = (e) => {
            if (e.candidate) sendICE(userId, e.candidate);
        };
        connect.ontrack = (e) => {
            if (e.streams.length > 0) {
                remoteVideo.srcObject = e.streams[0];
            }
        };
        connect.setRemoteDescription(session);
        connect.createAnswer().then((answer) => {
            http({
                url: "/message/send",
                data: {
                    to: userId,
                    content: [
                        {
                            type: "RTC:CB",
                            value: JSON.stringify(answer),
                        },
                    ],
                },
            });
            connect.setLocalDescription(answer);
            resolve(connect);
        });
    });
};

const sendICE = (userId: number, ice: RTCIceCandidate) => {
    http({
        url: "/message/send",
        data: {
            to: userId,
            content: [
                {
                    type: "RTC:ICE",
                    value: JSON.stringify(ice),
                },
            ],
        },
    });
};

export { createPeer, createAnswer };
