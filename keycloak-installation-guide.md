# Install and Configure Keycloak

1. Create a keycloak user

```
# groupadd -r keycloak
# useradd -r -g keycloak -d /opt/keycloak -s /sbin/nologin keycloak
```    

2. Install keycloak

```
# unzip keycloak-8.0.1.zip
# ln -s /opt/keycloak-8.0.1 /opt/keycloak
# chown -R keycloak:keycloak /opt/keycloak /opt/keycloak-8.0.1
```

3. Configure datasource

    For PostgreSQL, create the directory org/postgresql/main in the modules/system/layers/keycloak/ directory. Copy your database driver JAR into this directory and create an module.xml file within it too:
```
<?xml version="1.0" ?>
<module xmlns="urn:jboss:module:1.3" name="org.postgresql">
    <resources>
        <resource-root path="postgresql-42.2.5.jar"/>
    </resources>

    <dependencies>
        <module name="javax.api"/>
        <module name="javax.transaction.api"/>
    </dependencies>
</module>
```

    Declare Your JDBC Drivers and Modify the Keycloak Datasource:
    
```
<subsystem xmlns="urn:jboss:domain:datasources:5.0">
   ​<datasources>
     ...
     ​<datasource jndi-name="java:jboss/datasources/KeycloakDS" pool-name="KeycloakDS" enabled="true" use-java-context="true" statistics-enabled="${wildfly.datasources.statistics-enabled:${wildfly.statistics-enabled:false}}">
       <connection-url>jdbc:postgresql:localhost/keycloak</connection-url>
       <driver>postgresql</driver>
		<pool>
		   <max-pool-size>20</max-pool-size>
		</pool>
       <security>
           <user-name>username</user-name>
           <password>password</password>
       </security>
      </datasource>
     ​<drivers>
        ​<driver name="postgresql" module="org.postgresql">
            ​<xa-datasource-class>org.postgresql.xa.PGXADataSource</xa-datasource-class>
        ​</driver>
        ​<driver name="h2" module="com.h2database.h2">
            ​<xa-datasource-class>org.h2.jdbcx.JdbcDataSource</xa-datasource-class>
        ​</driver>
     ​</drivers>
   ​</datasources>
</subsystem>
```
4. Configure systemd
```
# mkdir /etc/keycloak
# cp /opt/keycloak/docs/contrib/scripts/systemd/wildfly.conf /etc/keycloak/keycloak.conf
# cp /opt/keycloak/docs/contrib/scripts/systemd/wildfly.service /etc/systemd/system/keycloak.service
# cp /opt/keycloak/docs/contrib/scripts/systemd/launch.sh /opt/keycloak/bin/
# chmod +x /opt/keycloak/bin/launch.sh
```
5. Start and enable keycloak
```
# systemctl start keycloak.service
# systemctl enable keycloak.service
```
6. Add keycloak user
```
# /opt/keycloak/bin/add-user-keycloak.sh -u admin
```
7. Configure Apache HTTP Server  (Optional)
```
ServerTokens Prod
Header always set Strict-Transport-Security "max-age=8640000; includeSubDomains; preload"
Header always append X-Frame-Options SAMEORIGIN

<VirtualHost *:443>
    ServerName   sso.itrunner.org
    ServerAlias  sso.itrunner.org
    ErrorLog     logs/keycloak_error_log
    TransferLog  logs/keycloak_access_log
    LogLevel warn

    SSLEngine on
    SSLProtocol all -SSLv2 -SSLv3
    SSLCipherSuite HIGH:3DES:!aNULL:!MD5:!SEED:!IDEA
    SSLCertificateFile /etc/pki/tls/certs/ca.crt
    SSLCertificateKeyFile /etc/pki/tls/private/ca.key

    RewriteEngine On
    RewriteCond %{DOCUMENT_ROOT}%{REQUEST_URI} -f [OR]
    RewriteCond %{DOCUMENT_ROOT}%{REQUEST_URI} -d
    RewriteRule ^ - [L]

    ProxyPreserveHost on
    ProxyPass /auth http://127.0.0.1:8080/auth timeout=600
    ProxyPassReverse /auth http://127.0.0.1:8080/auth
</VirtualHost>
```
Login keycloak https://sso.itrunner.org/auth

8. Add Realm

    * Name: heroes
    * Display name: Heroes SSO
    * HTML Display name: 
    ```
    <div class="kc-logo-text"><span>Heroes SSO</span></div>
    ```
	* Login > Require SSL: external requests
	* Themes > Internationalization Enabled: ON

9. Add Client

    * Client ID: heroes
    * Standard Flow Enabled: ON
    * Implicit Flow Enabled: OFF
    * Direct Access Grants Enabled: ON
    * Valid Redirect URIs: https://heroes.itrunner.org/*
    * Web Origins: heroes.itrunner.org

10. Add SAML 2.0 identity provider

    **ADFS**

	* Alias: adfs
	* Display Name: iTRunner - ADFS
	* Import from URL: https://adfs.itrunner.org/FederationMetadata/2007-06/FederationMetadata.xml -> Import
	* NameID Policy Format: Email
	* HTTP-POST Binding Response: ON
	* HTTP-POST Binding for AuthnRequest: ON
	* HTTP-POST Binding Logout: ON
	* Want AuthnRequests Signed: ON
	* Signature Algorithm: RSA_SHA256
	* SAML Signature Key Name: CERT_SUBJECT
	* Validate Signature: ON

	Mappers:

	firstName
	* Name: firstName
	* Mapper Type: Attribute Importer
	* Attribute Name: http://schemas.xmlsoap.org/ws/2005/05/identity/claims/givenname
	* Friendly Name: firstName
	* User Attribute Name: firstName

	lastName
	* Name: lastName
	* Mapper Type: Attribute Importer
	* Attribute Name: http://schemas.xmlsoap.org/ws/2005/05/identity/claims/surname
	* Friendly Name: lastName
	* User Attribute Name: lastName

	email
	* Name: email
	* Mapper Type: Attribute Importer
	* Attribute Name: http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress
	* Friendly Name: email
	* User Attribute Name: email

11. Add user and role