import './App.css';
import Navbar from './components/Home/Navbar';
import Home from './components/Home/Home';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Contact from './components/Home/Contact';
import About from './components/Home/About';
import Login from './components/Authpages/login';
import Signup from './components/Authpages/Signup';
import UserNotes from './components/Home/UserNotes';
import ProtectedRoute from './components/ProtectedRoute';
import NotePage from './components/Home/NotePage';
import Dashboard from './components/Dashboard/Dashboard';

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/about" element={<About />} />
          <Route path="/login" element={<Login />} />
          <Route path="/signup" element={<Signup />} />
          


          <Route path="/notes/*" 
          element={
          <ProtectedRoute>
          <UserNotes />
          </ProtectedRoute>
          } />
          <Route path="/notes/editor" element={<ProtectedRoute><NotePage /></ProtectedRoute>} />
          <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
