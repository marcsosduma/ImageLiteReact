# Estágio 1: Build
FROM node:24-alpine AS builder
WORKDIR /app
COPY package*.json ./
# Instala TODAS as dependências (incluindo devDependencies)
RUN npm install
COPY . .
# Agora o build vai funcionar porque as ferramentas estarão disponíveis
RUN npm run build

# Estágio 2: Runner (Imagem final mais leve)
FROM node:24-alpine AS runner
WORKDIR /app
COPY --from=builder /app/package*.json ./
# Aqui sim usamos --production para rodar apenas o que é necessário
RUN npm install --production
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/public ./public

EXPOSE 3000
USER node
ENTRYPOINT [ "npm", "start" ]