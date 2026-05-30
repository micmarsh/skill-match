from typing import Dict, List
from python_hiccup.html import render

skills_section_open = '<div id="skills-section">'
skills_section_close = "</div>"
skills_section_placeholder = skills_section_open + skills_section_close

SkillsDict = Dict[str, List[str]]

def per_skill_list(list_title, list_items):
    return ["div",
        ["div.header-3", list_title],
        ["ul.skills-list", 
            [["li", skill] for skill in list_items]]]

def skills_inner_html(skills: SkillsDict):
    return render(
    ["div#skills-section",
        ["h2", "Skills"],
        ["div", {"style": "display: flex; justify-content: space-around;"},
            [per_skill_list(list_title, skills[list_title]) for list_title in skills]]])

def render_template(template: str, skills:SkillsDict):
    return template.replace(skills_section_placeholder, skills_inner_html(skills))
