Simple java library for parsing placeholders within files.

Text: yml, json, txt, log \
Java: jar, class
##

```
Map<String, String> placeholders = Map.of(
    "%%__USER__%%", "Test_User_Placeholder", 
    "%%__NONCE__%%", String.valueOf(ThreadLocalRandom.current().nextInt(10000)));

Parseit.of(new File("path/to/file.jar")).setPlaceholders(placeholders).parse();
```
