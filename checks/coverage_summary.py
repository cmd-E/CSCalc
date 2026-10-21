"""Summarize the measured calculator core coverage in GitHub Actions."""
from pathlib import Path
import xml.etree.ElementTree as ET

report = Path(__file__).resolve().parents[1] / 'app/build/reports/jacoco/coreCoverageReport/coreCoverageReport.xml'
root = ET.parse(report).getroot()
print('## Calculator core coverage\n')
print('Android views are compiled and linted; the coverage gate applies to GradeCalculator.\n')
for counter in root.findall('counter'):
    if counter.attrib['type'] in ('LINE', 'BRANCH'):
        covered, missed = int(counter.attrib['covered']), int(counter.attrib['missed'])
        if covered + missed == 0:
            raise SystemExit('Empty coverage counter')
        print(f"- {counter.attrib['type']}: {covered}/{covered + missed} ({covered * 100 / (covered + missed):.2f}%)")
