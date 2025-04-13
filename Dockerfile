# 第一階段：構建應用程序
FROM maven:3.8.5-openjdk-17 AS build

# 設定工作目錄
WORKDIR /app

# 將 Maven 配置文件和源代碼複製到容器中
COPY pom.xml ./
COPY src ./src

# 執行 Maven 構建來生成 JAR 文件
RUN mvn clean package -DskipTests

# 第二階段：構建運行時映像
FROM openjdk:17-jdk-slim

# 設定工作目錄
WORKDIR /app

# 從構建階段複製 JAR 文件到運行時映像
COPY --from=build /app/target/mentor.jar mentor.jar

# 設定容器啟動時執行的命令
ENTRYPOINT ["java", "-jar", "mentor.jar"]

# 曝露端口
EXPOSE 8089
