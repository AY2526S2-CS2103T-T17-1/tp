import re

f1 = "src/main/java/seedu/address/logic/parser/AddressBookParser.java"
with open(f1, "r") as f: content = f.read()

# Fix case indentations
content = re.sub(r"^( {12})(case [a-zA-Z]+Command\.COMMAND_WORD:)", r"        \2", content, flags=re.MULTILINE)
content = re.sub(r"^( {16})(return |throw |logger\.finer)", r"            \2", content, flags=re.MULTILINE)

with open(f1, "w") as f: f.write(content)

f2 = "src/main/java/seedu/address/model/util/SampleDataUtil.java"
with open(f2, "r") as f: content = f.read()

lines = content.split('\n')
for i, line in enumerate(lines):
    if line.startswith("    public static Person[]"): continue
    if line.startswith("            return new Person"): lines[i] = "        return new Person[] {"
    elif line.startswith("                                new Person"): lines[i] = "            new Person" + line.split("new Person")[1]
    elif line.startswith("                                                new Email"): lines[i] = "                    new Email" + line.split("new Email")[1]
    elif line.startswith("                                                new Address"): lines[i] = "                    new Address" + line.split("new Address")[1]
    elif line.startswith("                                                getTagSet"): lines[i] = "                    getTagSet" + line.split("getTagSet")[1]
    elif line.startswith("                                                new Remark"): lines[i] = "                    new Remark" + line.split("new Remark")[1]
    elif line.startswith("            };"): lines[i] = "        };"
    elif line.startswith("    public static ReadOnlyAddressBook"): lines[i] = "    public static ReadOnlyAddressBook getSampleAddressBook() {"
    elif line.startswith("            AddressBook "): lines[i] = "        AddressBook sampleAb = new AddressBook();"
    elif line.startswith("            for "): lines[i] = "        for (Person samplePerson : getSamplePersons()) {"
    elif line.startswith("                        sampleAb"): lines[i] = "            sampleAb.addPerson(samplePerson);"
    elif line.startswith("            }"): lines[i] = "        }"
    elif line.startswith("            return sampleAb;"): lines[i] = "        return sampleAb;"
    elif line.startswith("    public static Set<Tag>"): lines[i] = "    public static Set<Tag> getTagSet(String... strings) {"
    elif line.startswith("            return Arrays"): lines[i] = "        return Arrays.stream(strings)"
    elif line.startswith("                                .map"): lines[i] = "                .map(Tag::new)"
    elif line.startswith("                                .collect"): lines[i] = "                .collect(Collectors.toSet());"
content = '\n'.join(lines)
with open(f2, "w") as f: f.write(content)

content = re.sub(r"^( {12})(default:)", r"        \2", content, flags=re.MULTILINE)
with open(f1, "w") as f: f.write(content)

