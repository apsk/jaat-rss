Notice, this reader is built with open-format-assumption: it depends only on 'item' and 'entry' tags of RSS and Atom respectively, and captures all nodes inside them into dictionaries, with nested levels flattened in keys by '.'-joining.
For example,

```xml
<person>
	<name>
		<first>
			Elvis
		</first>
		<last>
			Presley
		</last>
	</name>
	<uri>http://www.youtube.com/watch?v=gvlxRvhCB_A</uri>
</person>
```

is handled like

```java
entry.put("person.name.first", "Elvis");
entry.put("person.name.last", "Presley");
entry.put("person.uri", "http://www.youtube.com/watch?v=gvlxRvhCB_A");
```
