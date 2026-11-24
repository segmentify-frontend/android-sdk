// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';
import starlightThemeBlack from 'starlight-theme-black';

// https://astro.build/config
export default defineConfig({
  site: 'https://android-sdk-docs.segmentify.com',
  integrations: [
    starlight({
      plugins: [
        starlightThemeBlack({
          footerText:
            'Built & designed by [Segmentify](https://segmentify.com). The source code is available on [GitHub](https://github.com/segmentify/android-sdk).',
        }),
      ],
      logo: {
        src: './public/logo.svg',
      },
      title: 'Android SDK',
      description:
        'Documentation of the Segmentify Android SDK: installation, events, recommendations, search and push notifications.',
      customCss: ['./src/styles/custom.css'],
      social: [
        {
          icon: 'github',
          label: 'GitHub',
          href: 'https://github.com/segmentify/android-sdk',
        },
      ],
      // PageTitle adds the "Copy for LLMs" bar (see src/components/CopyForLLMs.astro).
      components: {
        PageTitle: './src/components/PageTitle.astro',
      },
      sidebar: [
        {
          label: 'Getting Started',
          items: [
            { label: 'Installation', slug: 'getting-started/installation' },
            { label: 'Configuration', slug: 'getting-started/configuration' },
            { label: 'Your First Integration', slug: 'getting-started/first-integration' },
          ],
        },
        {
          label: 'Events',
          items: [
            { label: 'Page View', slug: 'events/page-view' },
            { label: 'Product View', slug: 'events/product-view' },
            { label: 'Basket and Checkout', slug: 'events/basket-and-checkout' },
            { label: 'Wishlist', slug: 'events/wishlist' },
            { label: 'Search', slug: 'events/search' },
            { label: 'User Events', slug: 'events/user' },
            { label: 'Interactions and Banners', slug: 'events/interactions' },
            { label: 'Custom Event', slug: 'events/custom-event' },
          ],
        },
        {
          label: 'Features',
          items: [
            { label: 'Recommendations', slug: 'recommendations' },
            { label: 'Push Notifications', slug: 'push-notifications' },
          ],
        },
        {
          label: 'Help',
          items: [{ label: 'Troubleshooting', slug: 'troubleshooting' }],
        },
        {
          label: 'Related Documentation',
          items: [
            { label: 'API Docs', link: 'https://api-docs.segmentify.com/', attrs: { target: '_blank' } },
            { label: 'iOS SDK', link: 'https://ios-sdk-docs.segmentify.com/', attrs: { target: '_blank' } },
            { label: 'React Native SDK', link: 'https://rn-docs.segmentify.com/', attrs: { target: '_blank' } },
          ],
        },
        {
          label: 'Working with LLMs',
          items: [{ label: 'Copy for LLMs', slug: 'copy-for-llms' }],
        },
      ],
    }),
  ],
});
