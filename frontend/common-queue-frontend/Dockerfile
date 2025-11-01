FROM node:20-alpine AS build

WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build -- --configuration production

FROM nginx:1.27-alpine
RUN rm -rf /usr/share/nginx/html/* /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist/common-queue-frontend/ /usr/share/nginx/html/
COPY nginx.conf /etc/nginx/conf.d/app.conf

# Copy the wait-for script
COPY wait-for-backend.sh ./wait-for-backend.sh 
RUN chmod +x ./wait-for-backend.sh

EXPOSE 80
CMD ["/wait-for-backend.sh","nginx", "-g", "daemon off;"]
