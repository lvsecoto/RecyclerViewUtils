# RecyclerView Utils

## Getting started

### Setting up the dependency

Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
Step 2. Add the dependency
```groovy
    dependencies {
        implementation 'com.github.lvsecoto:recyclerviewutils:1.0'
    }
```

### Adding header and footer to RecyclerView
```java
    new HeaderFooterDecorator()
        .setAdapter(adapter)
        .setLayoutManager(layoutManager)
        .setHeaderView(headerView)
        .setFooterView(footerView)
        .decorate(recyclerView);
```
(For now only support vertical LinearLayoutManager and GridLayoutManager)
