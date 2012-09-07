Java application used to build the [schema module](https://github.com/loopasam/schema).

Usage (`launcher.ModuleBuilder`):

```java
package launcher;

import java.io.IOException;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import parser.Parser;
import parser.ParsingException;

public class ModuleBuilder {
    
    public static void main(String[] args) throws IOException, ParsingException, OWLOntologyCreationException {
	System.out.println("Starting parsing...");
	Parser parser = new Parser("data/microdata.txt", "Schema Draft Version 0.97");
	System.out.println("Parsing...");
	parser.parse();
	System.out.println("Get comments from owl file...");
	parser.getComments("data/schemaorg.owl");
	System.out.println("Print distribution of types in order to manually convert to map some to Java objects.");
	parser.printDistributions("distributions.txt");
	System.out.println("Export classes...");
	//Destination of the classes, here directly inside the module
	parser.saveSerializedClasses("/home/samuel/git/schema/app/models/schema");
	System.out.println("Export tags...");
	parser.saveSerializedTags("/home/samuel/git/schema/app/views/tags/schema");	
	System.out.println("Done!");
    }
}

```