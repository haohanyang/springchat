import React from "react";
import Contact from "./Contact";
import './ContactList.css'

function ContactList(prop: { contacts: string[] }) {

    return (
        <div className="col-md-6 col-lg-5 col-xl-4 chat-left">
            <div className="input-group chat-contact-search mb-3">
                <input type="text" className="form-control" placeholder="Search" />
                <div className="input-group-append">
                    <button className="btn" type="button">
                        <i className="fas fa-search"></i>
                    </button>
                </div>
            </div>
            <div className="border rounded chat-contact-list">
                <ul className="list-unstyled mb-0">
                    {prop.contacts.map((item, index) => {
                        return <Contact />
                    })}
                </ul>
            </div>
        </div>
    )
}

export default ContactList;