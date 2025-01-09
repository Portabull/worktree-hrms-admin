self.addEventListener('install', (event) => {
    console.log('Service Worker Installed');
    self.skipWaiting(); // Activate immediately
});

self.addEventListener('activate', (event) => {
    console.log('Service Worker Activated');
});

self.addEventListener('message', (event) => {
    if (event.data.action === 'scheduleNotification') {
        const delay = event.data.delay;

        setTimeout(() => {
            self.registration.showNotification('Hello!', {
                body: 'This is your scheduled notification!',
                icon: 'https://via.placeholder.com/128' // Optional icon
            });
        }, delay);
    }
});
