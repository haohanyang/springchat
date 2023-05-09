import Message from "./Message";

function MessageList(props: { Messages: string[] }) {
    return (
        <ul className="m-0 p-0">
            {props.Messages.map((message, _) => {
                return <Message content={message} time="now" />
            })}
        </ul>
    );
}

export default MessageList;