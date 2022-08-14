======================================================
Oracle Free Use Terms and Conditions (FUTC) License 
======================================================
https://www.oracle.com/downloads/licenses/oracle-free-license.html
===================================================================

ojdbc10-full.tar.gz - JDBC Thin Driver and Companion JARS
===========================================================
This TAR archive (ojdbc10-full.tar.gz) contains the 19.15 release of the Oracle JDBC Thin driver(ojdbc10.jar), the Universal Connection Pool (ucp.jar) and other companion JARs grouped by category. 

(1) ojdbc10.jar (4,500,694 bytes) - 
(SHA1 Checksum: 60f8246a9725f69b8aba74ba6301ca6d4a4580d3)
Oracle JDBC Driver compatible with JDK10 and JDK11;
(2) ucp.jar (1,690,150 bytes) - (SHA1 Checksum: 83b7d9fd9c67af2ef6c9204e30eb0027ae8e152c)
Universal Connection Pool classes for use with JDK8, JDK9, and JDK11 -- for performance, scalability, high availability, sharded and multitenant databases.
(3) ojdbc.policy (11,515 bytes) - Sample security policy file for Oracle Database JDBC drivers

======================
Security Related JARs
======================
Java applications require some additional jars to use Oracle Wallets. 
You need to use all the three jars while using Oracle Wallets. 

(4) oraclepki.jar (306,618 bytes) - (SHA1 Checksum: 46b5a28ca8758408950cfef46ef013ee0d70445d)
Additional jar required to access Oracle Wallets from Java
(5) osdt_cert.jar (210,336 bytes) - (SHA1 Checksum: 5e505ddfff926dba06e3f269d78e1882e9cec732)
Additional jar required to access Oracle Wallets from Java
(6) osdt_core.jar (312,281 bytes) - (SHA1 Checksum: b1c83e62bb4d174734aac19b0ddbce0ed89fb25e)
Additional jar required to access Oracle Wallets from Java

=============================
JARs for NLS and XDK support 
=============================
(7) orai18n.jar (1,663,954 bytes) - (SHA1 Checksum: 3744bc9f42a2d809f926d6d0d7ec44718d66b53f) 
Classes for NLS support
(8) xdb.jar (265,580 bytes) - (SHA1 Checksum: 23b599c8fc66ac9958ecb9fab7685e38d3df0ffd)
Classes to support standard JDBC 4.x java.sql.SQLXML interface 
(9) xmlparserv2.jar (1,935,495 bytes) - (SHA1 Checksum: 30761c81576dbdc6a9dea5e9a612c25d51bb621a)
Classes to support standard JDBC 4.x java.sql.SQLXML interface 
(10) xmlparserv2_sans_jaxp_services.jar (1,932,962 bytes) - (SHA1 Checksum: 443158bb117349e166e8c43b87943695d7159722) 
Classes to support standard JDBC 4.x java.sql.SQLXML interface
====================================================
JARs for Real Application Clusters(RAC), ADG, or DG 
====================================================
(11) ons.jar (156,244 bytes) - (SHA1 Checksum: 721b8113d2da1360ef5ee9868022bfe11a13dd1d)
for use by the pure Java client-side Oracle Notification Services (ONS) daemon
(12) simplefan.jar (32,169 bytes) - (SHA1 Checksum: f44cade4be457467fdef50024bc428dc5a0f72ed)
Java APIs for subscribing to RAC events via ONS; simplefan policy and javadoc

===========================================================================================
NOTE: The diagnosability JARs **SHOULD NOT** be used in the production environment. 
These JARs (ojdbc10_g.jar,ojdbc10dms.jar, ojdbc10dms_g.jar) are meant to be used in the 
development, testing, or pre-production environment to diagnose any JDBC related issues. 

=====================================
OJDBC - Diagnosability Related JARs
=====================================
(13) ojdbc10_g.jar (7,547,710 bytes) - (SHA1 Checksum: 12db2992a039b3746447588643b78281bcb7f2a1)
Same as ojdbc10.jar except compiled with "javac -g" and contains tracing code.

(14) ojdbc10dms.jar (6,272,221 bytes) - (SHA1 Checksum: 061900630992a81b15d0df1bdf58b5bdb6f4bc44)
Same as ojdbc10.jar, except that it contains instrumentation to support DMS and limited java.util.logging calls.

(15) ojdbc10dms_g.jar (7,577,395 bytes) - (SHA1 Checksum: 8834757e5f286d71d5e0f676940bd3a42f60e115shasum)
Same as ojdbc10_g.jar except that it contains instrumentation to support DMS.

(16) dms.jar (2,194,305 bytes) - (SHA1 Checksum: 07219d0fec3645d70a5824a803503399b74c5331)
dms.jar required for DMS-enabled JAR files.

==================================================================
Oracle JDBC and UCP - Javadoc and README
==================================================================

(17) JDBC-Javadoc-19c.jar (2,313,742 bytes) - JDBC API Reference 19c

(18) UCP-Javadoc-19c.jar (372,019 bytes) - UCP Java API Reference 19c

(19) simplefan-Javadoc-19c.jar (88,476 bytes) - Simplefan API Reference 19c 

(20) xdb-Javadoc-19c.jar (2,861,664 bytes) - XDB API Reference 19c 

(21) xmlparserv2-Javadoc-19c.jar (2,861,664 bytes) - xmlparserv2 API Reference 19c 

(22) JDBC-Readme.txt: It contains general information about the JDBC driver and bugs that have been fixed in the 19.15 release. 

(23) UCP-Readme.txt: It contains general information about UCP and bugs that are fixed in the 19.15 release. 

(24) Bugs-fixed-in-1915.txt: It contains the list of bugs fixed in the 19.15 release. 
=================
USAGE GUIDELINES
=================
Refer to the JDBC Developers Guide (https://docs.oracle.com/en/database/oracle/oracle-database/19/jjdbc/index.html) and Universal Connection Pool Developers Guide (https://docs.oracle.com/en/database/oracle/oracle-database/19/jjucp/index.html) for more details. 
