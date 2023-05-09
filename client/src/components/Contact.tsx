import React from "react";
import './Contact.css'

function Contact() {

    const [unreadMessageCounts, setUnreadMessageCounts] = React.useState(0);
    const onClick = () => { }

    return <li className="p-2 border-bottom contact-list-item" onClick={onClick} >
        <a className="d-flex justify-content-between text-decoration-none">
            <div className="d-flex flex-row">
                <div>
                    <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava1-bg.webp" alt="avatar"
                        className="d-flex align-self-center me-3" width="60" />
                </div>
                <div className="pt-1">
                    <p className="fw-bold mb-0">@User.FirstName @User.LastName</p>
                    <p className="small text-muted"> @LastMessage?.Content</p>
                </div>
            </div>
            <div className="pt-1">
                <p className="small text-muted mb-1">
                    @LastMessage?.Time
                </p>
                <span className="badge bg-danger rounded-pill float-end">{unreadMessageCounts}</span>
            </div>
        </a>
    </li >
}

export default Contact;