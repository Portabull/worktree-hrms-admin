// Install Event
self.addEventListener('install', (event) => {
    console.log('[DEBUG] Service Worker: Install event fired.');
    event.waitUntil(self.skipWaiting()); // Activate immediately
    console.log('[DEBUG] Service Worker: skipWaiting() called.');
});

// Activate Event
self.addEventListener('activate', (event) => {
    console.log('[DEBUG] Service Worker: Activate event fired.');
    event.waitUntil(self.clients.claim()); // Claim control of pages
    console.log('[DEBUG] Service Worker: clients.claim() called.');
});

// Fetch Event (for debugging or caching logic)
self.addEventListener('fetch', (event) => {
    console.log('[DEBUG] Service Worker: Fetch event for:', event.request.url);
});

// Push Event (for handling push notifications)
self.addEventListener('push', (event) => {
    console.log('[DEBUG] Service Worker: Push event received:', event);
    const title = 'Push Notification';
    const options = {
        body: 'This is a notification triggered by a push event.',
        icon: 'image/logo.png', // Replace with your icon path
        badge: 'image/logo.png' // Replace with your badge path
    };
    event.waitUntil(self.registration.showNotification(title, options));
    console.log('[DEBUG] Service Worker: Notification displayed.');
});

// Notification Click Event (Optional, for debugging notification clicks)
self.addEventListener('notificationclick', (event) => {
    console.log('[DEBUG] Notification clicked:', event.notification);
    event.notification.close();
    event.waitUntil(
        clients.openWindow('/') // Replace with the URL you want to open
    );
});
