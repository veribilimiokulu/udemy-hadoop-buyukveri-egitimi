#  NiFi Mysql’den Kafka’ya incremental veri akışı 

## Mysql veri tabanında hazırlık yap.
 
### Kullanılacak iris veri setini indir 
```	
cd /home/maria_dev
wget https://raw.githubusercontent.com/erkansirin78/datasets/master/iris_with_pk.csv 
```
ile  /home/maria_dev diznine veri ardışık artan Id sütununa sahip iris veri setini indir.

### Mysql’e bağlan
```	
mysql -u root -p
Enter password: hortonworks1
```

### azhadoop veri tabanını seç yoksa yarat
```
mysql> create database if not exists azhadoop;

mysql> use azhadoop;
```

### tablo oluştur 
```
mysql> create table 
iris_with_pk(Id int  NOT NULL AUTO_INCREMENT, 
SepalLengthCm double, 
SepalWidthCm double, 
PetalLengthCm double, 
PetalWidthCm double, 
Species VARCHAR(20), 
PRIMARY KEY (Id));  
```
	
### csv dosyasını tablo içine aktar
```
mysql> load data local infile 
'/home/maria_dev/iris_with_pk.csv' 
into table iris_with_pk 
fields terminated by ',' 
enclosed by '"' 
lines terminated by '\n'  
(Id,SepalLengthCm,SepalWidthCm,PetalLengthCm,PetalWidthCm,Species); 
```
Beklenen çıktı:
```
Query OK, 150 rows affected (0.01 sec)
Records: 150  Deleted: 0  Skipped: 0  Warnings: 0
```
	

##	Nifi Arayüzü Ön Hazırlıklar:
###	 Nifi UI
Browser’a ` http://sandbox.hortonworks.com:8090/nifi/ ` adresini gir ve NiFi arayüzünü gör. 

### mysql bağlantısı için connection pool oluştur

Ana sayfa -> sol menü configüration (küçük çark) -> controller service tabı -> sağ tarafta + işareti -> DBCPConnectionPool -> Configure
	 
- Properties tabına:
```
Database connection url: jdbc:mysql://127.0.0.1/azhadoop
Database Driver Class Name: com.mysql.jdbc.Driver
Database Driver Location(s): /usr/share/java/mysql-connector-java.jar
Database User: root
Password: hortonworks1
```
- Sağ taraftan enable et (şimşek işareti)

 
## Kafka Topic Hazırlıklar:
### Topic oluşturma
```
/usr/hdp/current/kafka-broker/bin/kafka-topics.sh --create \
--zookeeper sandbox.hortonworks.com:2181 \
--replication-factor 1 \
--partitions 1 \
--topic iris
```

### Kafka Producer
```
/usr/hdp/current/kafka-broker/bin/kafka-console-producer.sh \
--broker-list sandbox.hortonworks.com:6667 \
--topic iris
```

###	Kafka Consumer: Farklı bir terminalden
```
/usr/hdp/current/kafka-broker/bin/kafka-console-consumer.sh \
--bootstrap-server sandbox.hortonworks.com:6667 \
--topic iris --from-beginning
```

### Producer ve Consumer test
Kafka Producer’dan örnek mesajlar yazarak çalışırlığını kontrol etme


## Nifi Arayüzünde Veri Akışı Oluşturma

### Nifi arayüzüne git

### Process Group oluştur
Yukarı menüden bir adet Process Group oluştur. (Sürükle bırak) İsmine `MysqlToKafka` verelim.
Çift tıklayıp içine girelim.

### Processor QueryDatabaseTable
- Add Processor -> QueryDatabaseTable oluştur

- Sağ tıkla -> Configure -> Properties sekmesi

	DatabaseConnectionPoolingService için yukarıda oluşturduğumuz connection poolu seçiyoruz.
```
Database Type= Generic
Table Name = iris_with_pk
Columns to return = Hepsini seçmek istiyorsak boş kalıyor.
Maximum-value columns = Id (incremental bir number olmalı)
Max Wait time = 0
Fetch size = 2
Max rows per flow file = 2
Output batch size = 2 
Geri kalan özellikler varsayılan
```	

### Processor ConvertAvroToJson
QueryDatabaseTable Processoru'nü ConvertAvroToJson'a bağlıyoruz.
	
### Processor PublishKafka_0_10 

ConvertAvroToJson PublishKafka_0_10 bağla ve success kutucuğunu işaretle

Sağ tıkla configure 
	
Kafka brokers = sandbox-hdp.hortonworks.com:6667
topic name = iris
	
Settings tabından failure ve success kutucuklarını işaretle
	
	
### Çalıştırma

iris topiğini dinleyen Kafka-Console Consumer açık iken 

Tek tek processor'leri sağ tıklayıp başlatalım.









