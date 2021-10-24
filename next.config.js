/** @type {import('next').NextConfig} */
module.exports = {
  reactStrictMode: true,
  webpack: (config) => {
    config.experiments = {topLevelAwait: true};
    return config;
  },
  i18n: {
    locales: ['fr-FR'],
    defaultLocale: 'fr-FR'
  },
}
