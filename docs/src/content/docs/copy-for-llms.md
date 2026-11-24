---
title: Copy for LLMs
description: Give this documentation, or a Google Doc, to an LLM as clean Markdown
---

LLMs such as Claude or ChatGPT read Markdown far better than a copied web page or a Word-style document: headings, lists, tables and code blocks keep their structure, and no navigation, styling or long URLs waste the context window. This site, and the Google Docs we write alongside it, can both be handed to an LLM as Markdown.

## Copy a page of this site

- **Copy for LLMs**: the button under the title of every page copies the page as Markdown (title, description and content). Paste it into the LLM.
- **View as Markdown**: every page is also served as a Markdown file: replace the trailing slash of the page address with `.md`, so `…/some-page/` becomes `…/some-page.md`.
- **llms.txt**: [`llms.txt`](../llms.txt) lists every page of the site with a link to its Markdown file, following the [llms.txt](https://llmstxt.org/) convention. Give its address to an LLM or agent that can browse.
- **llms-full.txt**: [`llms-full.txt`](../llms-full.txt) is the whole site in one Markdown file, for a tool that should read all of it at once.

## Export a Google Doc as Markdown (Google Docs Copy for LLMs)

Specs, analyses and meeting notes often start in Google Docs. To give one to an LLM, or to move it into this documentation, export it as Markdown with the **Better Google Docs Copy** Chrome extension (its project is called "Google Docs Copy for LLMs"):

1. Install it from the [Chrome Web Store](https://chromewebstore.google.com/detail/better-google-docs-copy/faciokbjemdddkjokcajndenapikgcml).
2. Open the Google Doc and click **Copy for LLM**.
3. The first time, sign in with your Google account and allow access. The extension reads the document through the Google Docs API and its comments through the Google Drive API.
4. Choose whether to copy the selected text or the whole document, then paste the Markdown where you need it.

What it adds over a plain copy:

- **Comments**: comment threads come along with the passage they were left on, so the LLM sees the review discussion too.
- **Images**: Google's very long image URLs are replaced with short references, so they do not fill the context window.
- **Options** (right-click the extension icon, then Options): heading style (ATX `# Heading` or Setext), comment format (XML tags, blockquote or quoted context) and whether resolved comments are included.

The extension runs in the browser and only talks to Google's APIs. Before pasting a document into an external LLM, check that it holds no customer data, credentials or other information that must not leave the company.

### Without the extension

Google Docs can export Markdown itself, without comments:

- **Whole document**: File → Download → Markdown (.md).
- **A selection**: turn on Tools → Preferences → Enable Markdown, then use Edit → Copy as Markdown.

## Move a Google Doc into this documentation

1. Export the doc as Markdown as described above (leave comments out, or resolve them first).
2. Save it as a new `.md` file under `src/content/docs/`, with `title` and `description` front matter.
3. Add the page to the `sidebar` in `astro.config.mjs`, and open a pull request.
