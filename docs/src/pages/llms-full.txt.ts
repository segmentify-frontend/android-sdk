import type { APIRoute } from 'astro';
import { getDocs, pageMarkdown, textResponse } from '../lib/llm-markdown';

export const GET: APIRoute = async () => {
  const docs = await getDocs();
  return textResponse(docs.map(pageMarkdown).join('\n---\n\n'));
};
