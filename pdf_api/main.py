
from fastapi import FastAPI, Response
from pydantic import BaseModel
from typing import List
from utilities import render_template, weasyprint_render

app = FastAPI()

class HtmlRender(BaseModel):
    body: str

#TODO maybe dynamic filename based on hash of content or something or other?
pdf_headers = {"Content-Disposition": "inline; filename=Rendered.pdf"}

@app.post("/render_raw")
async def render_pdf(html: HtmlRender):
    pdf_bytes = weasyprint_render(html.body)
    return Response(pdf_bytes, media_type="application/pdf", headers=pdf_headers)

class ResumeRender(BaseModel):
    skills: List[str]

# TODO more robust, command-line arg/dependency injection(?)-based thing!
template:str
with open("/home/michael/Documents/JobSearch/Resume_Template.html") as f:
    template = f.read()

@app.post("render_resume")
async def render_resume(input: ResumeRender):
    full_resume_text = render_template(template, input.skills)
    pdf_bytes = weasyprint_render(html.body)
    return Response(pdf_bytes, media_type="application/pdf", headers=pdf_headers)