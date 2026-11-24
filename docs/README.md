# Android SDK documentation

The documentation site of the Segmentify Android SDK, built with Astro + Starlight like the other Segmentify documentation sites.

| | Command (run inside `docs/`) |
| --- | --- |
| Install | `pnpm install` |
| Dev | `pnpm dev` |
| Build | `pnpm build` (outputs to `docs/dist`) |

- Pages are Markdown files under `src/content/docs/`. A new page must also be added to the `sidebar` in `astro.config.mjs`.
- Every page has a "Copy for LLMs" button and a Markdown copy; the site also serves `llms.txt` and `llms-full.txt`.
- Deployed on Vercel with `docs/` as the project's root directory. A push that does not change `docs/` skips the build.

When the SDK's public API changes, update the matching page in the same pull request. Check method and property names against the source, not against other pages.
