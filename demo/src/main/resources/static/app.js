let stompClient = null;
let token = null;
let currentRoomId = null;
let currentUser = null;

// ---------------------- AUTH ------------------------------

function registerUser() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(res => res.json())
    .then(data => {
        alert("Registration Successful!");
        console.log(data);
    })
    .catch(err => console.error(err));
}

function loginUser() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(res => res.json())
    .then(data => {
        token = data.token;
        currentUser = data.username;

        connectWebSocket();
        loadRooms();

        alert("Login Successful!");
    })
    .catch(err => console.error(err));
}

// ---------------------- ROOMS ------------------------------

function createRoom() {
    const name = document.getElementById("roomName").value;

    fetch("/api/room", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify({ name })
    })
    .then(() => loadRooms());
}

function loadRooms() {
    fetch("/api/room", {
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => res.json())
    .then(rooms => {
        const list = document.getElementById("rooms-list");
        list.innerHTML = "";

        rooms.forEach(room => {
            const btn = document.createElement("button");
            btn.className = "btn btn-outline-primary w-100 mb-2";
            btn.textContent = room.name;

            btn.onclick = () => joinRoom(room.id, room.name);
            list.appendChild(btn);
        });
    });
}

// auto-refresh rooms
setInterval(() => {
    if (token) loadRooms();
}, 5000);

// ---------------------- WEBSOCKETS ------------------------------

function connectWebSocket() {
    const socket = new SockJS("/ws");
    stompClient = Stomp.over(socket);

    stompClient.connect({ Authorization: "Bearer " + token }, () => {
        console.log("Connected to WebSocket");
    });
}

function joinRoom(roomId, roomName) {
    currentRoomId = roomId;
    document.getElementById("chatRoomTitle").innerText = roomName;

    document.getElementById("messages").innerHTML = "";

    // Load history
    fetch(`/api/message/room/${roomId}`, {
        headers: { "Authorization": "Bearer " + token }
    })
    .then(res => res.json())
    .then(messages => {
        messages.forEach(m => addMessage(m.content, m.sender));
    });

    // Subscribe to new messages
    stompClient.subscribe(`/topic/room/${roomId}`, msg => {
        const message = JSON.parse(msg.body);
        addMessage(message.content, message.sender);
    });
}

function sendMessage() {
    if (!currentRoomId) return alert("Select a chat room first!");

    const content = document.getElementById("messageInput").value;

    stompClient.send("/app/chat", {}, JSON.stringify({
        roomId: currentRoomId,
        sender: currentUser,
        content: content
    }));

    document.getElementById("messageInput").value = "";
}

// ---------------------- UI ------------------------------

function addMessage(content, sender) {
    const container = document.getElementById("messages");

    const div = document.createElement("div");
    div.className = "msg-bubble " + (sender === currentUser ? "msg-me" : "msg-other");
    div.textContent = `${sender}: ${content}`;

    container.appendChild(div);
    container.scrollTop = container.scrollHeight;
}
