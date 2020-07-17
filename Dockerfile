FROM maven:3.6.3-jdk-8

# Google Chrome

ARG CHROME_VERSION=83.0.4103.61-1
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - \
        && echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list \        && apt-get update -qqy \
        && apt-get -qqy install google-chrome-stable=$CHROME_VERSION \
        && rm /etc/apt/sources.list.d/google-chrome.list \
        && rm -rf /var/lib/apt/lists/* /var/cache/apt/* \
        && sed -i 's/"$HERE\/chrome"/"$HERE\/chrome" --no-sandbox/g' /opt/google/chrome/google-chrome
RUN mvn clean install
CMD mvn spring-boot:run -pl backend
