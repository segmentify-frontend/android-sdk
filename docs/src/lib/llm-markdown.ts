// Builds the Markdown that the "Copy for LLMs" outputs serve: one file per page,
// llms.txt (an index of those files) and llms-full.txt (every page in one file).
// See the "Copy for LLMs" page of this site for how these are meant to be used.
import { getCollection, type CollectionEntry } from 'astro:content';

export type DocsEntry = CollectionEntry<'docs'>;

const base = import.meta.env.BASE_URL.replace(/\/?$/, '/');

/**
 * The page's slug as it appears in its URL: `01-introduction`, `intro/prerequisites`,
 * `index` for the home page. Older Starlight versions keep the file extension in the id.
 */
export function markdownSlug(entry: DocsEntry): string {
  const slug = entry.id
    .replace(/\.(md|mdx|mdoc)$/i, '')
    .replace(/(^|\/)index$/, '')
    .toLowerCase();
  return slug === '' ? 'index' : slug;
}

export async function getDocs(): Promise<DocsEntry[]> {
  const docs = await getCollection('docs', entry => !entry.data.draft);
  // Home page first, this "Copy for LLMs" guide last, the rest by path.
  const ranks: Record<string, number> = {
    index: 0,
    'getting-started/installation': 0.1,
    'getting-started/configuration': 0.2,
    'getting-started/first-integration': 0.3,
    'copy-for-llms': 2,
  };
  const rank = (entry: DocsEntry) => ranks[markdownSlug(entry)] ?? 1;
  return docs.sort(
    (a, b) =>
      rank(a) - rank(b) ||
      markdownSlug(a).localeCompare(markdownSlug(b), 'en', { numeric: true })
  );
}

/** The URL path of a page's Markdown copy, e.g. `/01-introduction.md` or `/index.md`. */
export function markdownPath(entry: DocsEntry): string {
  return `${base}${markdownSlug(entry)}.md`;
}

export function pageMarkdown(entry: DocsEntry): string {
  const { title, description } = entry.data;
  const header = [`# ${title}`, description ? `\n> ${description}` : ''].join(
    '\n'
  );
  return `${header}\n\n${(entry.body ?? '').trim()}\n`;
}

export function markdownResponse(body: string): Response {
  return new Response(body, {
    headers: { 'Content-Type': 'text/markdown; charset=utf-8' },
  });
}

export function textResponse(body: string): Response {
  return new Response(body, {
    headers: { 'Content-Type': 'text/plain; charset=utf-8' },
  });
}
