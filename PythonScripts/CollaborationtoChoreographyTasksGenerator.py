import xml.etree.ElementTree as ET

FileName = "XMLDocuments/FromCollToChor/Input/BirthCertificateIssueCollab.xml"

mytree = ET.parse(FileName)
myroot = mytree.getroot()

contract = myroot.get("name") + "ChoreographyDiagram" + ".xml"

root = ET.Element("choreographydiagram", name = myroot.get("name"))
root1 = ET.Element("choreographytasks")

for x in myroot.iter('messageflow'):
    task = ET.Element("choreographytask", name=x.attrib["name"])
    tag_1 = ET.SubElement(task, 'message')
    tag_1.text = x.attrib['content']
    tag_2 = ET.SubElement(task, 'initiator')
    tag_2.text = x.find('sender').text
    tag_3 = ET.SubElement(task, 'receivers')
    tag_3.text = x.find('receiver').text
    root1.append(task)
root.append(root1)


with open(f"XMLDocuments/FromCollToChor/Output/{contract}", "w") as files:
    files.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>")


root2 = ET.Element("participants")
for x in myroot.iter('participant'):
    part = ET.Element('participant', id = x.attrib["id"])
    part.text = x.text
    root2.append(part)

root.append(root2)
tree = ET.ElementTree(root)
with open(contract, "ab") as files:
    tree.write(files)