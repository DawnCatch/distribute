import MarkdownIt from "markdown-it";
import hljs from "highlight.js";

import { full as emoji } from "markdown-it-emoji";

const markdown = new MarkdownIt({
    highlight: function (str, lang) {
        try {
            var currLang: string;
            if (!lang || !hljs.getLanguage(lang)) {
                currLang = "ts";
            } else {
                currLang = lang;
            }
            var result = `<pre><details open><summary>${lang}</summary><code>`;
            const lines = str.split("\n");
            lines.forEach((value, index) => {
                const line = value + (index === lines.length - 1 ? "" : "\n");
                if (line) {
                    result += `<span class="code_line" data-line-num="${index}">${
                        hljs.highlight(line, { language: currLang }).value
                    }</span>`;
                }
            });
            return result + "</code></details></pre>";
        } catch (__) {}
        return "";
    },
    breaks: true
});
markdown.use(emoji);

export default markdown;
