import type { APIRoute, GetStaticPaths } from 'astro';
import {
  getDocs,
  markdownResponse,
  markdownSlug,
  pageMarkdown,
  type DocsEntry,
} from '../lib/llm-markdown';

export const getStaticPaths = (async () => {
  const docs = await getDocs();
  return docs.map(entry => ({
    params: { slug: markdownSlug(entry) },
    props: { entry },
  }));
}) satisfies GetStaticPaths;

export const GET: APIRoute<{ entry: DocsEntry }> = ({ props }) =>
  markdownResponse(pageMarkdown(props.entry));
