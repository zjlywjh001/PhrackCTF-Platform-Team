#PhrackCTF-Platform-Team

This is the full version of phrackCTF-Platform including backend & frontend. This platform is for team competition, user register as single person and then create team to take part in competition. Personal version is here:[https://github.com/zjlywjh001/PhrackCTF-Platform-Personal](https://github.com/zjlywjh001/PhrackCTF-Platform-Personal)   
虽然我不能保证这是前端最炫的CTF平台，但我相信这会是后台功能最友好的CTF平台。

Based on Spring and SpringMVC framework.

###Demo
See demo photos here [http://photo.163.com/zjlywjh002/#m=1&aid=303094421&p=1](http://photo.163.com/zjlywjh002/#m=1&aid=303094421&p=1)
##Techniques

###Base Framework
spring & spring MVC
###Database Connect Pool:
Alibaba Druid
###SQL mapper framework
Mybatis
###Security framework
Apache Shrio

###Database
postgresql-9.5

###FrontEnd
Bootstrap & jQuery

##Notice
It's highly recommanded to use https when deploy this platform.  
Before using :  
1. Install umeditor maven dependencies commons-fileupload-1.2.2.jar and ueditor-mini.jar in src/main/webapp/umeditor/jsp to local Maven repository:   
**mvn install:install-file -Dfile=path_to_umeditor_mini_jar\ueditor-mini.jar -DgroupId=umeditor -DartifactId=ueditor-mini -Dversion=1.2.2 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true**   
**mvn install:install-file -Dfile=path_to_commons_fileupload_jar\commons-fileupload-1.2.2.jar -DgroupId=umeditor -DartifactId=commons-fileupload -Dversion=1.2.2 -Dpackaging=jar -DgeneratePom=true -DcreateChecksum=true**   
1. Set mail server info in resources/spring-mail.xml because this platform using mail system to activate user.   
2. Set database information in system.properties   
3. Mail template in top.phrack.ctf.utils.MailUtil   

##License
Apache Public License v2.