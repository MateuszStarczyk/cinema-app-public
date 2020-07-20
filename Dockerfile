FROM maven:3.6.3-jdk-8
COPY . /app
WORKDIR /app

# Google Chrome
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
        && echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \        && apt-get update -qqy \
        && apt-get -qqy install google-chrome-stable \
        && rm /etc/apt/sources.list.d/google-chrome.list \
        && rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
        && sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome
RUN mvn clean install
CMD java -jar  backend/target/cinema-app-backend-0.0.1-SNAPSHOT.jar  --server.port=$PORT
