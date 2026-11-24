import type { APIRoute } from 'astro';
import config from 'virtual:starlight/user-config';
import { getDocs, markdownPath, textResponse } from '../lib/llm-markdown';

const base = import.meta.env.BASE_URL.replace(/\/?$/, '/');

export const GET: APIRoute = async () => {
  const docs = await getDocs();
  const lines = [
    `# ${typeof config.title === 'string' ? config.title : Object.values(config.title)[0]}`,
    '',
    config.description ? `> ${config.description}` : '',
    '',
    `Every page of this site is also served as Markdown. The whole site in one file: [llms-full.txt](${base}llms-full.txt)`,
    '',
    '## Pages',
    '',
    ...docs.map(entry => {
      const description = entry.data.description
        ? `: ${entry.data.description}`
        : '';
      return `- [${entry.data.title}](${markdownPath(entry)})${description}`;
    }),
    '',
  ];
  return textResponse(lines.join('\n'));
};
