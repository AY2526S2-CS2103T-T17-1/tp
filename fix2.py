import re

f1 = "src/main/java/seedu/address/logic/parser/AddressBookParser.java"
with open(f1, "r") as f: content = f.read()

content = re.sub(r"^( {12})(default:)", r"        \2", content, flags=re.MULTILINE)

with open(f1, "w") as f: f.write(content)
