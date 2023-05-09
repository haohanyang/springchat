import React from "react";

function ContactStatus() {

    return (<div className="d-flex justify-content-between align-items-center chat-contact-status mb-3" id="contact-info">
        <div className="d-flex">
            <div className="p-2">
                <img src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava1-bg.webp" alt="avatar"
                    width="40" />
            </div>
            <div className="d-flex flex-column justify-content-center align-items-start">
                <div>
                    <span className="contact-status-name">@CurrentContact?.FirstName @CurrentContact?.LastName</span>
                    <span className="contact-status-username text-secondary">@@ @CurrentContact?.Username</span>
                </div>

                <span className="badge text-bg-success contact-status">Online</span>
            </div>
        </div>
        <div className="dropdown">
            <button className="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
            </button>
            <ul className="dropdown-menu">
                <li><a className="dropdown-item" href="#">Action</a></li>
                <li><a className="dropdown-item" href="#">Another action</a></li>
                <li><a className="dropdown-item" href="#">Something else here</a></li>
            </ul>
        </div>
    </div>)
}

export default ContactStatus;