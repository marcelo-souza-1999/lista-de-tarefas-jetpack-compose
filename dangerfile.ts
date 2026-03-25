import { danger, warn, fail, message } from "danger"

const bigPRThreshold = 600;
if (danger.github.pr.additions + danger.github.deletions > bigPRThreshold) {
  warn("⚠️ Este PR parece um pouco grande. Talvez fosse melhor dividi-lo em partes menores?");
}

const hasAppChanges = danger.git.modified_files.some(path => path.startsWith("app/src/main"));
const hasTestChanges = danger.git.modified_files.some(path => path.includes("test/"));

if (hasAppChanges && !hasTestChanges) {
  warn("🔍 Notei alterações no código, mas nenhum novo teste. Lembra-te de manter a nossa cobertura!");
}

if (danger.github.pr.title.includes("WIP")) {
  fail("🚫 Este PR ainda está marcado como Trabalho em Progresso (WIP).");
}

message("✨ Obrigado por este contributo, Marcelo! A análise estática está a ser processada.");