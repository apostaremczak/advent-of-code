DAY := $(shell date +%d)
DAY_PADDED := $(shell printf "%02d" $(DAY))

MAIN_SRC := src/main/java/pl/apostaremczak/aoc/Day$(DAY_PADDED).java
TEST_SRC := src/test/java/pl/apostaremczak/aoc/Day$(DAY_PADDED)Tests.java

all: copy-templates download-input

copy-templates:
	@echo "Copying templates for day $(DAY_PADDED)"
	cp template/Day.java.template $(MAIN_SRC)
	cp template/DayTests.java.template $(TEST_SRC)
	sed -i '' 's/NUM/$(DAY_PADDED)/g' $(MAIN_SRC)
	sed -i '' 's/NUM/$(DAY_PADDED)/g' $(TEST_SRC)
	@echo "Templates copied and updated for Day $(DAY_PADDED)"

download-input:
	aoc download -I -d $(DAY)
	mv input src/main/resources/$(DAY_PADDED).txt
