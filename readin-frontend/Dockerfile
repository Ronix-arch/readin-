FROM node:23-alpine AS build
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY ./ ./
# The API URL is no longer needed here, as Nginx will handle the proxy.
RUN npm run build

FROM nginx:stable-alpine
# Remove the default Nginx configuration.
RUN rm /etc/nginx/conf.d/default.conf
# Copy our custom Nginx configuration.
COPY nginx.conf /etc/nginx/conf.d/
# Copy the built React app.
COPY --from=build /usr/src/app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
