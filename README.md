## LazyTable
Two dimension Lazy load library for compose. Use LazyTable function to create your own 2D Grid

### How to use
Add following repository for dependency resolution. Since we are using github packages you might need to add your github username and password in environment variable or github.properties file in keys mentioned in code block.
```
        maven {
            url "https://maven.pkg.github.com/sachin-bijalwan/LazyTableCompose"

            def propsFile = new File("${rootProject.projectDir}/github.properties")
            def props = new Properties()
            props.load(new FileInputStream(propsFile))




            credentials {
                username = props.getProperty("gpr.user") ?: System.getenv("USERNAME")



                password = props.getProperty("gpr.token") ?: System.getenv("TOKEN")
            }
        }
```

Once dependencies are synced you can invoke LazyTable composable. 

### Features
LazyTable as the name suggest is a Lazily loading 2D grid Composable. It can contain infinite number of rows/cols and will work perfectly fine. 

Apart from this it also supports adding Sticky Header and Columns. There are two parameters exposed in LazyTable which can be used for passing Rows/Columns

### UI
#### Without Headers
https://github.com/user-attachments/assets/dd2bac2a-0d02-4466-b418-a02ecc88176f
#### With headers
https://github.com/user-attachments/assets/a723c6dd-a00f-4ed1-b118-e05436215d31

