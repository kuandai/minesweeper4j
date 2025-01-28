# Define variables
SRC_DIR = src
BUILD_DIR = build
BIN_DIR = bin
ASSETS_DIR = assets
LIBS_DIR = libs/lwjgl
MAIN_CLASS = com.marlin.App  # Update with your actual main class
JAR_NAME = $(BIN_DIR)/game.jar
SPACE := $(empty) $(empty) # ;-;

# Detect OS and set classpath separator
ifeq ($(OS),Windows_NT)
    CP_SEP = ;
else
    CP_SEP = :
endif

# LWJGL JARs
LWJGL_JARS = $(wildcard $(LIBS_DIR)/*.jar)
LWJGL_NATIVE_DIR = $(LIBS_DIR)  # Assuming native JARs are in the same directory

# Build classpath with the correct separator
LWJGL_CLASSPATH = $(shell echo "$(LWJGL_JARS)" | sed 's/ /$(CP_SEP)/g')

# Find all Java source files
SRC_FILES = $(shell find $(SRC_DIR) -name '*.java')


# Default target
all: $(JAR_NAME)

# Compile all source files at once to resolve dependencies
compile:
	@mkdir -p $(BUILD_DIR)
	javac -d $(BUILD_DIR) -cp "$(LWJGL_CLASSPATH)" $(SRC_FILES)

# Build the jar file
$(JAR_NAME): compile
	@mkdir -p $(BIN_DIR)
	@cp -r $(ASSETS_DIR) $(BUILD_DIR)/assets
	@for jarfile in $(LWJGL_JARS); do unzip -o "$$jarfile" -d $(BUILD_DIR); done
	echo "Manifest-Version: 1.0" > manifest.tmp
	echo "Main-Class: $(MAIN_CLASS)" >> manifest.tmp
	jar cfm $(JAR_NAME) manifest.tmp -C $(BUILD_DIR) .
	rm -rf manifest.tmp

# Clean up
clean:
	rm -rf $(BUILD_DIR) $(BIN_DIR)

# Run the program
run: $(JAR_NAME)
	java -cp "$(LWJGL_CLASSPATH)$(CP_SEP)$(JAR_NAME)" \
	     -Djava.library.path=$(LIBS_DIR) \
	     $(MAIN_CLASS)

.PHONY: all compile clean run