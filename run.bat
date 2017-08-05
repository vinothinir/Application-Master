cd C:\Users\KumarR114\git\cds-SecureFamily-e2e-test\SecureNet\
::call ant compile
java -Ddevice.name=%2  -cp ".\lib\*";.\classes org.junit.runner.JUnitCore com.vodafone.securefamily.tests.%1
::cd %7
::java -Ddevice.name=%2 -Ddevice.name.spouse=%3 -Ddevice.name.child=%4 -Dopco=%5 -Ddata.index=%6 -::Dgetdevicelogmandatory=%8 -cp ".\lib\*";.\classes org.junit.runner.JUnitCore com.vodafone.securefamily.tests.%1

::pause

