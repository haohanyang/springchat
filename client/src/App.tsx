import React from 'react';
import './App.css';
import ContactList from './components/ContactList';
import ChatBox from './components/ChatBox';

function App() {
  const [loading, setLoading] = React.useState(false);

  const content = loading ?
    <div className="spinner-border" role="status">
    </div> :
    <div className="col-md-12">
      <div className="card" id="chat3">
        <div className="card-body">
          <div className="row pt-2">
            <ContactList contacts={["", "", "", ""]} />
            <ChatBox />
          </div>
        </div>
      </div>
    </div >

  return <main role="main" className="container">
    {content}
  </main>
}


export default App;
