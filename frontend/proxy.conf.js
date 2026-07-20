module.exports = [
  {
    context: ['/api/v1/finance'],
    target: 'http://localhost:8085',
    secure: false,
    changeOrigin: true,
  },
  {
    context: ['/api/v1/portfolio'],
    target: 'http://localhost:8086',
    secure: false,
    changeOrigin: true,
  },
  {
    context: ['/api'],
    target: 'http://localhost:8081',
    secure: false,
    changeOrigin: true,
  },
];
