module.exports = [
  {
    context: ['/api/v1/finance'],
    target: 'http://localhost:8084',
    secure: false,
    changeOrigin: true,
  },
  {
    context: ['/api'],
    target: 'http://localhost:8080',
    secure: false,
    changeOrigin: true,
  },
];
