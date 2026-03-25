#!/bin/bash

# Cores para o terminal
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # Sem cor

echo -e "${BLUE}==== Iniciando Suíte de Testes - Lista de Tarefas ====${NC}"

# 1. Limpeza do ambiente
echo -e "\n${BLUE}[1/4] Limpando build anterior...${NC}"
./gradlew clean

# 2. Testes de Unidade (Lógica, ViewModels, UseCases)
echo -e "\n${BLUE}[2/4] Rodando Testes de Unidade (JUnit/MockK)...${NC}"
./gradlew testDebugUnitTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Testes de Unidade passaram!${NC}"
else
    echo -e "${RED}❌ Testes de Unidade falharam.${NC}"
    exit 1
fi

# 3. Testes de Instrumentação (Comportamento na UI)
echo -e "\n${BLUE}[3/4] Rodando Testes de Instrumentação (JUnit/Compose)...${NC}"
./gradlew connectedDebugAndroidTest
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Testes de Instrumentação passaram!${NC}"
else
    echo -e "${RED}❌ Testes de Instrumentação falharam.${NC}"
    exit 1
fi

# 4. Testes de Screenshot (Visual/Shot)
echo -e "\n${BLUE}[4/4] Verificando integridade visual (Screenshot Tests)...${NC}"
./gradlew executeScreenshotTests
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ Integridade visual confirmada!${NC}"
else
    echo -e "${RED}❌ Diferença visual detectada! Verifique o relatório em: app/build/reports/shot/debug/index.html${NC}"
    exit 1
fi

echo -e "\n${GREEN}==== Tudo certo! App testado com sucesso. ====${NC}"