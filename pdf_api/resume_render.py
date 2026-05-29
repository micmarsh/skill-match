from typing import Dict, List


skills_section_open = '<div id="skills-section">'
skills_section_close = "</div>"
skills_section_placeholder = skills_section_open + skills_section_close

SkillsDict = Dict[str, List[str]]

def skills_inner_html(skills: SkillsDict):
    raise Exception("todo: re-implement html generation and thus whole resume generation on this backend!?")
    return ""

def render_template(template: str, skills:SkillsDict):
    return template.replace(skills_section_placeholder, skills_inner_html(skills))
