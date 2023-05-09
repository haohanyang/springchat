
import React, { useState } from 'react';
import ContactStatus from './ContactStatus';
import MessageList from './MessageList';
import './ChatBox.css';

function ChatBox() {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [sending, setSending] = useState(false);
    const [input, setInput] = useState("");
    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => setInput(e.target.value);

    const sendMessage = async () => {
    }

    const content = loading ?
        <div className="spinner-border" role="status" >
            <span className="visually-hidden" > Loading...</span >
        </div > :
        (error != null ?
            <div className="alert alert-danger" role="alert" >
                error
            </div> : <MessageList Messages={["message", "message", "message", "message", "message"]} />)

    return <div className="col-md-6 col-lg-7 col-xl-8 chat-right">
        <ContactStatus />
        <div className="border rounded p-2 chat-message-box">
            {content}
        </div>
        <div className="text-muted d-flex justify-content-between align-items-center pe-3 pt-3 mt-2">
            <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava6-bg.webp" alt="avatar"
                className="input-avatar" />
            <div className="form-outline ml-2 flex-fill">
                <input type="text" className="form-control form-control" id="message-input" value={input} onChange={handleInputChange} />
            </div>
            <div>
                <button className="ms-1 text-muted btn m-0 p-0 file-button"><i className="bi bi-paperclip"></i></button>
                <button className="ms-3 text-muted btn m-0 p-0 emoji-button"><i
                    className="bi bi-emoji-smile"></i></button>
                <button className={"ms-3 btn m-0 p-0 send-btn " + sending ? "disabled" : ""} onClick={sendMessage}> <i className="bi bi-send"></i></button>
            </div >
        </div >
    </div >
}

export default ChatBox;