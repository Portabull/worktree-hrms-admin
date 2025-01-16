importScripts('https://www.gstatic.com/firebasejs/11.1.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/11.1.0/firebase-messaging.js');

// Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyA_ii6KwNuN8bIzeK7V8IkBzBzMFjXni8Q",
  authDomain: "worktree-hrms.firebaseapp.com",
  projectId: "worktree-hrms",
  storageBucket: "worktree-hrms.appspot.com", // Corrected storageBucket
  messagingSenderId: "559305051722",
  appId: "1:559305051722:web:64ac18194578e66ea260b3",
  measurementId: "G-4QLF75ZPYT"
};

// Initialize Firebase
firebase.initializeApp(firebaseConfig);
const messaging = firebase.messaging();

// Background message handler
messaging.onBackgroundMessage((payload) => {
  console.log('Received background message:', payload);
  const notificationTitle = payload.notification.title;
  const notificationOptions = {
    body: payload.notification.body,
    icon: payload.notification.icon,
  };
  self.registration.showNotification(notificationTitle, notificationOptions);
});
