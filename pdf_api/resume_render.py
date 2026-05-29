from typing import Dict, List
from yattag import Doc

skills_section_open = '<div id="skills-section">'
skills_section_close = "</div>"
skills_section_placeholder = skills_section_open + skills_section_close

SkillsDict = Dict[str, List[str]]

def skills_inner_html(skills: SkillsDict):
    doc, tag, text, line = Doc().ttl()

    with tag("div", id="skills-section"):
        line("h2", "Skills")
        with tag("div", style='display: flex; justify-content: space-around;'):
            for list_title in skills:
                with tag("div"):
                    line("div", list_title, klass="header-3")
                    with tag("ul", klass="skills-list"):
                        for skill in skills[list_title]:
                            line("li", skill)

    return doc.getvalue()

def render_template(template: str, skills:SkillsDict):
    return template.replace(skills_section_placeholder, skills_inner_html(skills))
