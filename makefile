# A general java project makefile

# Set the file name of your jar package:
JAR_PKG = Skyscraper.jar

# Set your entry point of your java app:
ENTRY_POINT = Skyscraper

RES_DIR = yes

SOURCE_FILES = test/Skyscraper.java

# Set your java compiler here:
JAVAC = javac

JFLAGS = -encoding UTF-8


#-------------------------------------------------------------------#

vpath %.class bin
vpath %.java src

# show help message by default
Default:
	@echo "make build: compile your source file into bin folder."
	@echo "make jar: package your project into a executable jar."
	@echo "use command java -jar Skyscraper.jar [option] to run app."

build: $(SOURCE_FILES:.java=.class)

%.class: %.java
	$(JAVAC) -cp bin -d bin $(JFLAGS) $<

rebuild: clean build

.PHONY: new clean run jar

new:
ifeq ($(RES_DIR),yes)
	mkdir -pv src bin res
else
	mkdir -pv src bin
endif

clean:
	rm -frv bin/*

run:
	java -cp bin $(ENTRY_POINT)

jar:
ifeq ($(RES_DIR),yes)
	jar cvfe $(JAR_PKG) $(ENTRY_POINT)  -C bin . res
else
	jar cvfe $(JAR_PKG) $(ENTRY_POINT) -C bin .
endif
