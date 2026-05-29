from typing import List
from weasyprint import HTML


def weasyprint_render(html: str):
    return HTML(string=html).render().write_pdf()

def render_template(template: str, skills: List[str]):
    return ""
