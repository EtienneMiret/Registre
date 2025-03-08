/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  rewrites: async () => [
    {
      source: '/api/:path*',
      destination: `${process.env.REGISTRE_BACKEND_URL}/:path*`,
    }
  ]
};

export default nextConfig;
