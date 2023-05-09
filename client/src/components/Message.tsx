import './Message.css'

function Message(props: { content: string, time: string }) {
    return <div className="d-flex flex-row justify-content-start">
        <img className="message-avatar" src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-chat/ava6-bg.webp" alt="avatar 1" />
        <div>
            <p className="message-content small p-2 ms-3 mb-1 rounded-3">{props.content}</p>
            <p className="message-time small ms-3 mb-3 rounded-3 text-muted float-end">{props.time}</p>
        </div>
    </div>
}

export default Message;